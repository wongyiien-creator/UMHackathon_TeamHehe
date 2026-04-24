import asyncio
from datetime import datetime
import firebase_admin
from firebase_admin import firestore, credentials, initialize_app


try:
    db = firestore.client()
except Exception:
    cred = credentials.ApplicationDefault()
    firebase_admin.initialize_app(cred, {
        'projectId': "umhack-493910",
    })
        
    pass

MOCK_DATA = [
    {"name": "Q1 Revenue", "content": "Total: 50000 MYR. Source: Retail Sales."},
    {"name": "Farmer Feedback", "content": "The irrigation system in Block B is leaking. Needs repair by Friday."},
    {"name": "Inventory Log", "content": "Seed count: 500 bags. Fertilizer: 20 units remaining."},
    {"name": "Market Price Update", "content": "Padi prices rose by 5% in the local Selangor market today."},
    {"name": "Strategy Note", "content": "Consider expanding to Shah Alam region by Q3 2026."},
    {"name": "Expense Record", "content": "Fuel costs: 1200 MYR. Logistics: 850 MYR."},
    {"name": "Staff Meeting", "content": "Discussed AI integration for crop yield optimization with the team."},
    {"name": "Weather Warning", "content": "Heavy rain expected next week. Secure the storage facility."},
    {"name": "Equipment Maintenance", "content": "Tractor #4 serviced. Next checkup in 3 months."},
    {"name": "Customer Lead", "content": "New interest from a wholesaler in Port Klang. Follow up ASAP."}
]

async def seed_direct(user_id: str):
    print(f"Starting Seed for User: {user_id}")
    for item in MOCK_DATA:
        try:
            doc_ref = db.collection("users").document(user_id).collection("data").document()
            doc_ref.set({
                "data_id": doc_ref.id,
                "data_name": item["name"],
                "content": item["content"],
                "timestamp": datetime.now()
            })
            print(f"Seeded: {item['name']}")
        except Exception as e:
            print(f"Failed! : {e}")
    print("\nComplete!.")

if __name__ == "__main__":
    loop = asyncio.get_event_loop()
    loop.create_task(seed_direct("q5t5SFbMhwHqmHLNxgon"))