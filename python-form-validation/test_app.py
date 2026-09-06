from app import validate_signup

def test_valid_user():
    ok, errs = validate_signup("john", "john@example.com", "secret123")
    assert ok is True
    assert len(errs) == 0

def test_invalid_email():
    ok, errs = validate_signup("john", "invalid-email", "secret123")
    assert ok is False
    assert "Invalid email" in errs[0]