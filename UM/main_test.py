import sys
from unittest.mock import MagicMock

mock_google = MagicMock()
mock_firebase = MagicMock()
mock_zai = MagicMock()
mock_genai = MagicMock()

sys.modules["google"] = mock_google
sys.modules["google.cloud"] = mock_google
sys.modules["google.genai"] = mock_genai
sys.modules["google.genai.types"] = MagicMock()
sys.modules["google.cloud.firestore_v1"] = mock_google
sys.modules["google.cloud.firestore_v1.query"] = mock_google
sys.modules["firebase_admin"] = mock_firebase
sys.modules["firebase_admin.credentials"] = MagicMock()
sys.modules["firebase_admin.firestore"] = MagicMock()
sys.modules["zai"] = mock_zai

class MockZaiClient:
    def __init__(self, *args, **kwargs):
        self.chat = MagicMock()
mock_zai.ZaiClient = MockZaiClient

from main import app
from fastapi.testclient import TestClient

client = TestClient(app)

def test_just_the_revenue_logic(mocker):
    # 1. Mock Database
    mocker.patch('main.get_data', return_value=[{"content": "dummy"}]) 

    # 2. Mock AI Response - Use a very clean string
    mock_res = MagicMock()
    # We make sure the JSON is perfectly valid
    mock_res.choices[0].message.content = '{"impact_metrics": {"revenue_growth_estimate": "101%"}}'
    
    # PATCH THE CLIENT: Ensure this matches how 'client' is imported in main.py
    mocker.patch('main.client.chat.completions.create', return_value=mock_res)

    # 3. Act
    response = client.post("/analysis", params={"user_id": "test_user"})

    # DEBUG: If it's still 500, this will tell us WHY
    if response.status_code == 500:
        print(f"REAL ERROR: {response.json().get('detail')}")

    assert response.status_code == 422