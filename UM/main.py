import os
from fastapi import FastAPI, HTTPException
import firebase_admin
from firebase_admin import credentials, firestore
from google.cloud.firestore_v1 import FieldFilter
import json
from pydantic import BaseModel
from typing import Any
import re
from google.cloud.firestore_v1.query import Query
from datetime import datetime, timedelta
from google import genai
from google.genai import types
import requests


app = FastAPI()

ZAI_KEY = os.environ.get("ZAI_API_KEY")
PROJECT_ID = os.environ.get("GOOGLE_CLOUD_PROJECT", "umhack-493910")
BASE_URL = "https://api.ilmu.ai/v1/chat/completions"

headers = {
    "Authorization": f"Bearer {ZAI_KEY}",
    "Content-Type": "application/json"
}


# Initialize Firebase Admin
if not firebase_admin._apps:
    cred = credentials.ApplicationDefault()
    firebase_admin.initialize_app(cred, {
        'projectId': PROJECT_ID,
    })

db = firestore.client()


#Vertex AI
client_V = genai.Client(
    vertexai=True, 
    project=PROJECT_ID, 
    location="us-central1"
)

class AnalysisRequest(BaseModel):
    user_id: str

@app.post("/analysis")
async def analyze_business_data(request: AnalysisRequest):
    try:
        data_list = await get_data(request.user_id)
        print(data_list)
        print("Data Retrieved!")
        
        #testing
        print(f"DEBUG: Data received from Firestore: {data_list}")
        
        formatted_data = ""
        for item in data_list:
            formatted_data += f"""
            data id: {item.get('data_id')}
            data name: {item.get('data_name')}
            content: {item.get('content')}
            -------------------------------"""
        
        prompt = f"""
        Analyze the following business data for a Malaysian SME:
         
         Data:
         Data: {formatted_data}
         
         TASK:
         Perform a deep synthesis. You must return your findings in a JSON format strictly following the schema below. 
         Do not include any conversational text outside of the JSON.
         
         SCHEMA:
         {{
           "market_analysis": {{
             "white_spaces": [
               {{ "opportunity": "title", "explanation": "short explanation" }}
             ],
             "sentiment_correlation": {{
                 "observation": "how sentiment explains trends",
                 "explanation": "detailed reasoning"
             }},
             "hidden_truth": "The non-obvious reality derived from data",
             "forecasted_direction": {{
                 "prediction": "Predicted market move",
                 "explanation": "short explanation"
             }}
           }},
           "marketing_mix_strategy": {{
             "product": {{ "strategy": "Product strategy", "explanation": "reasoning" }},
             "price": {{ "strategy": "Pricing strategy", "explanation": "reasoning" }},
             "place": {{ "strategy": "Distribution strategy", "explanation": "reasoning" }},
             "promotion": {{ "strategy": "Promotion strategy", "explanation": "reasoning" }}
           }},
           "forecasted_challenges": [
             "Challenge 1",
             "Challenge 2"
           ],
           "precaution_plan": [
             "Step 1",
             "Step 2"
           ],
           "impact_metrics": {{
             "forecasted_sales": "Predicted sales impact",
             "revenue_growth_estimate": "Percentage eg. 15%"
           }}
         }}
         """
         
        payload = {
                    "model": "ilmu-glm-5.1",  
                    "messages": [
                    {
                        "role": "system",
                        "content": "Your are a market analyist for Buisnesses"
                    },
                    {
                        "role": "user",
                        "content": prompt
                    }
                ]
        }

        response = requests.post(BASE_URL, headers=headers, json=payload)
        
        default_json = {
           "market_analysis": {
               "white_spaces": [],
               "sentiment_correlation": {"observation": "N/A", "explanation": "Insufficient data"},
               "hidden_truth": "Data synthesis unavailable",
               "forecasted_direction": {"prediction": "Neutral", "explanation": "N/A"}
           },
           "marketing_mix_strategy": {
               "product": {"strategy": "N/A", "explanation": "N/A"},
               "price": {"strategy": "N/A", "explanation": "N/A"},
               "place": {"strategy": "N/A", "explanation": "N/A"},
               "promotion": {"strategy": "N/A", "explanation": "N/A"}
           },
           "forecasted_challenges": ["Insufficient data for analysis"],
           "precaution_plan": ["Review source data"],
           "impact_metrics": {
               "forecasted_sales": "0",
               "revenue_growth_estimate": "0%"
           }
       }
        
        result = response.json()
        if "error" in result:
            error_msg = result["error"].get("message", "Unknown API Error")
            print(f"!!! API returned an error: {error_msg}")
            return default_json

        if "choices" not in result:
            print(f"!!! Unexpected Response Format. Keys received: {list(result.keys())}")
            print(f"Full Response Body: {result}") 
            return default_json
        content = result['choices'][0]['message']['content'].strip()

        if not content:
            return default_json

        if content.startswith("```"):
            content = re.sub(r'^```json\s*|```$', '', content, flags=re.MULTILINE).strip()

        try:
            analysis_data = json.loads(content)
            metrics = analysis_data.get("impact_metrics") or {}
            revenue_str = metrics.get("revenue_growth_estimate") or "0"
            revenue_number = re.findall(r"[-+]?\d*\.\d+|\d+", str(revenue_str))

            if revenue_number:
                revenue_number = float(revenue_number[0])
            else:
                revenue_number = 0.0
        
            if revenue_number > 100:
                raise HTTPException(
                    status_code=422, 
                    detail="The AI generated an unrealistic forecast. Please try again."
                )
        
            print("Successfully parsed analysis data!")
            print(analysis_data)
            return analysis_data
        
        except json.JSONDecodeError as e:
            print(f"JSON Parsing failed: {e}")
            return default_json 
        
    except HTTPException as http_err:
        print(f"{str(http_err)}")
        raise http_err
    
    except Exception as e:
        print(f"Error Message: {str(e)}")
        
        raise HTTPException(status_code=500, detail=str(e))   


class MarketingMix(BaseModel):
    product: str
    price: str
    place: str
    promotion: str

class ComparisonRequest(BaseModel):
    marketing_mix_1: MarketingMix
    marketing_mix_2: MarketingMix
    region: str
    industry: str
        
@app.post("/comparison")
async def compare_marketing_mix(request: ComparisonRequest):
    try:
        context = await vertexSearch(request.region, request.industry)

        
        mix_1 = request.marketing_mix_1
        mix_2 = request.marketing_mix_2
        
        print(mix_1)
        print(mix_2)
        
        prompt= f""" 
        Compare the folliwing marketing mix strategy
        
        Marketing mix strategy:
        Strategy 1: {mix_1}
        Strategt 2: {mix_2}
        
        Context:
        {context}
        
        Task:
        Perform a comparison between the 2 marketing mix strategy. You must return your findings in a JSON format strictly following the schema below. 
        Do not include any conversational text outside of the JSON.
        
        Schema:
        {{
          "stress_test": {{
            "scenario": "A likely market disruption (e.g., price war or supply delay)",
            "plan_a_resilience": "How Plan A survives the scenario",
            "plan_b_resilience": "How Plan B survives the scenario"
          }},
          "comparison": {{
            "plan_a": {{ "pros": ["", ""], "cons": ["", ""] }},
            "plan_b": {{ "pros": ["", ""], "cons": ["", ""] }}
          }},
          "final_decision": {{
            "chosen_plan": "Plan A or Plan B",
            "reasoning": "Data-driven explanation for the choice",
            "roi_probability": "High/Medium/Low"
          }},
          "optimization_plan": [
            "Improvement 1 for the chosen plan",
            "Improvement 2 for the chosen plan"
          ]
        }}
        """
        
        payload = {
                    "model": "ilmu-glm-5.1",  
                    "messages": [
                    {
                        "role": "system",
                        "content": "Your are a market analyist for Buisnesses"
                    },
                    {
                        "role": "user",
                        "content": prompt
                    }
                ],
                "temperature": 0.2,  
        }

        response = requests.post(BASE_URL, headers=headers, json=payload, timeout=120)
        
        default_json = {
        "stress_test": {
            "scenario": "Market stability (Default Scenario)",
            "plan_a_resilience": "Resilience data currently unavailable.",
            "plan_b_resilience": "Resilience data currently unavailable."
        },
        "comparison": {
            "plan_a": {
                "pros": ["Data pending"],
                "cons": ["Data pending"]
            },
            "plan_b": {
                "pros": ["Data pending"],
                "cons": ["Data pending"]
            }
        },
        "final_decision": {
            "chosen_plan": "Pending Analysis",
            "reasoning": "Insufficient data provided to make a definitive decision.",
            "roi_probability": "Low/Medium (Awaiting Data)"
        },
        "optimization_plan": [
            "Re-run analysis with more specific business data",
            "Verify input parameters for market disruption"
        ]
    }
        
        if not response.text or not response.text.strip():
            print("!!! API returned an empty body (char 0 error)")
            return default_json

        if response.status_code != 200:
            print(f"!!! API Error {response.status_code}: {response.text}")
            return default_json

        try:
            result = response.json()
            if 'choices' not in result:
                print(f"!!! Unexpected JSON structure: {result}")
                return default_json
                
            content = result['choices'][0]['message']['content'].strip()
        except Exception as e:
            print(f"!!! Failed to parse initial API JSON: {e}")
            return default_json

        if not content:
            return default_json

        if content.startswith("```"):
            content = re.sub(r'^```json\s*|```$', '', content, flags=re.MULTILINE).strip()

        try:
            comparison_data = json.loads(content)
            print("Successfully parsed comparison data!")
            return comparison_data
        except json.JSONDecodeError as e:
            print(f"AI content was not valid JSON: {e}")
            return default_json
    
    
    except Exception as e:
        print(f"Error Message: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))
        return default_json
    
   
class DataPayload(BaseModel):
    data_name: str
    content: Any 

class DataPayloadResponse(BaseModel):
    data_id: str
    status: bool   

@app.post("/save_data/{user_id}")
async def save_data(user_id: str, payload: DataPayload):
    try:
        doc_ref = db.collection("users").document(user_id) \
               .collection("data").document()
        doc_ref.set({
            "data_id": doc_ref.id,
            "data_name": payload.data_name,
            "content": payload.content,
            "timestamp": datetime.now()
         })
        
        return {"data_id": doc_ref.id, "error": True}
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Firestore Error: {str(e)}")
    

@app.post("/get_data/{user_id}")
async def get_data(user_id: str):
    try:
        thirty_days_ago = datetime.now() - timedelta(days=30)
        
        docs = db.collection("users").document(user_id)\
         .collection("data")\
         .where(filter=FieldFilter("timestamp", ">=", thirty_days_ago))\
         .order_by("timestamp", direction=Query.DESCENDING)\
         .limit(10)\
         .stream()
         
        all_data = []
        for doc in docs:
            data = doc.to_dict()
            all_data.append(data)
        if not all_data:
            #get last upload data regardless of data
            return await get_fallback_data(user_id)
        
        return all_data
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Firestore Error: {str(e)}")

async def get_fallback_data(user_id: str):
    docs = db.collection("users").document(user_id)\
             .collection("data")\
             .order_by("timestamp", direction=Query.DESCENDING)\
             .limit(3)\
             .stream()
    
    all_data = []
    for doc in docs:
        data = doc.to_dict()
        all_data.append(data)
    if not all_data:
        #get last upload data regardless of data
        return None
    return all_data

async def vertexSearch(region: str, industry: str):
    print("start Vertex Search")
    prompt = f"""Get market analysis for {industry} in {region}. Trends, competitors, and sentiment.
    
    You must return your findings in a JSON format strictly following the schema below. 
    Do not include any conversational text outside of the JSON. Keep each answer short, 1-2 sentence.
    
   
    Schema:
    {{
      "trend": {{
        "status": "",
        "top": ["", "", ""],
        "drivers": ""
      }},
    
      "competitors": {{
        "direct": [
          {{"name": "", "strength": ""}}
        ],
        "indirect": ["", ""]
      }}
    }}
    
    Rules:
    - "" for unknown strings
    - [] for unknown lists
    - never omit keys
    - no extra text
    - direct and indirect must contain max 2 items only. If fewer exist, return fewer. Never exceed 5.
    - at most 200 words
    """
    
    try:
        response = client_V.models.generate_content(
            model="gemini-2.5-flash",
            contents=prompt,
            config=types.GenerateContentConfig(
                tools=[types.Tool(google_search=types.GoogleSearch())]
            )
        )
        
        raw = response.text or ""

        if not raw.strip():
            return {"error": "empty_response"}
        
        clean = re.sub(r"```json|```", "", raw).strip()
        
        try:
            print(json.loads(clean))
            return json.loads(clean)
        except json.JSONDecodeError:
            return {
                "error": "invalid_json",
                "raw": raw
            }
        
    
    except Exception as e:
        print(f"Vertex Error: {e}")
        return "Search failed."

class LoginInput(BaseModel):
    username: str
    password: str
    
class LoginResponse(BaseModel):
    user_id: str 
    error: bool
    
@app.post("/login", response_model=LoginResponse)
def login(login_data: LoginInput):
    try:
        query = (
                db.collection("users")
                .where("username", "==", login_data.username)
                .where("password", "==", login_data.password)
                .get()
                )
        
        if not query:  
            return {"user_id": None, "error": False}
        
        else:
            doc = query[0]
            user_id = doc.id
            username = doc.to_dict()["username"]
            
            print("login Complete!")
            return {"user_id": doc.id, "error": True}
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Firestore Error: {str(e)}")
    
    
class SignupInput(BaseModel):
    username: str
    password: str

class SignUpResponse(BaseModel):
    user_id: str 
    error: bool

@app.post("/signup", response_model=SignUpResponse)
def signup(signup_data: SignupInput):
    try:
        #check for duplicates
        duplicate = db.collection("users").where("username", "==", signup_data.username).get()
        
        if len(duplicate) > 0:  
            return {"user_id": None, "error": False}
           
        else:
            doc_ref = db.collection("users").document()
            doc_ref.set({
                "user_id:": doc_ref.id,
                "username": signup_data.username,
                "password": signup_data.password
            })
            return {"user_id": doc_ref.id, "error": True}
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Firestore Error: {str(e)}")
        





