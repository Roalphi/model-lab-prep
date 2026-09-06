from flask import Flask, render_template, request

app = Flask(__name__)

recipe_db = [
    {"title": "Pasta Carbonara", "ingredients": "pasta, eggs, cheese, bacon"},
    {"title": "Tomato Soup", "ingredients": "tomato, water, salt, butter"},
    {"title": "Pancakes", "ingredients": "flour, milk, eggs, sugar"}
]

def search_recipes(query):
    if not query or not query.strip():
        return recipe_db
    q = query.strip().lower()
    return [r for r in recipe_db if q in r["title"].lower() or q in r["ingredients"].lower()]

@app.route('/', methods=['GET', 'POST'])
def index():
    results = recipe_db
    if request.method == 'POST':
        query = request.form.get('query')
        results = search_recipes(query)
    return render_template('index.html', recipes=results)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)