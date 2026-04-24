import requests
import json

# Your Configuration
API_KEY = ""  
BASE_URL = "https://api.ilmu.ai/v1/chat/completions"

# The request data
headers = {
    "Authorization": f"Bearer {API_KEY}",
    "Content-Type": "application/json"
}

payload = {
    "model": "ilmu-glm-5.1",  #
    "messages": [
        {"role": "user", "content": "Hello! Are you ILMU-GLM-5.1?"}
    ]
}

try:
    # Sending the request
    response = requests.post(BASE_URL, headers=headers, json=payload)
    
    # Check if successful
    if response.status_code == 200:
        result = response.json()
        print("--- Response Received ---")
        print(result['choices'][0]['message']['content'])
    else:
        print(f"Error {response.status_code}: {response.text}")

except Exception as e:
    print(f"An error occurred: {e}")