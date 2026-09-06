import pytest
from app import app, search_word

def test_search_existing_word():
    word, meaning = search_word("python")
    assert word == "python"
    assert "programming language" in meaning

def test_search_non_existing_word():
    word, meaning = search_word("java")
    assert word == "java"
    assert meaning == "Word not found in dictionary."

def test_search_empty_input():
    word, meaning = search_word("   ")
    assert word is None
    assert meaning == "Please enter a word to search."

def test_flask_route():
    client = app.test_client()
    response = client.get('/')
    assert response.status_code == 200
    assert b"Online Dictionary" in response.data