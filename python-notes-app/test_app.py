import pytest
from app import app, notes_db, add_note

def test_add_note_success():
    initial_count = len(notes_db)
    result = add_note("Test Sticky Note")
    assert result is True
    assert len(notes_db) == initial_count + 1

def test_add_empty_note_fails():
    result = add_note("   ")
    assert result is False

def test_routes():
    client = app.test_client()
    response = client.get('/')
    assert response.status_code == 200
    assert b"Sticky Notes Web App" in response.data