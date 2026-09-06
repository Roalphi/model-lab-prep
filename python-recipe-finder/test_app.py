import pytest
from app import app, search_recipes

def test_search():
    res = search_recipes("pasta")
    assert len(res) == 1
    assert res[0]["title"] == "Pasta Carbonara"

def test_flask_route():
    client = app.test_client()
    assert client.get('/').status_code == 200