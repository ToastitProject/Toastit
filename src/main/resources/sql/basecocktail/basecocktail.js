const cocktail = {
    strDrink: "Margarita",
    strAlcoholic: "Alcoholic",
    strCategory: "Cocktail",
    strGlass: "Cocktail glass",
    strIngredient1: "Tequila",
    strIngredient2: "Triple sec",
    strIngredient3: "Lime juice",
    strIngredient4: null,
    strIngredient5: null,
    strIngredient6: null,
    strIngredient7: null,
    strIngredient8: null,
    strIngredient9: null,
    strIngredient10: null,
    strIngredient11: null,
    strInstructions: "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it.",
    strMeasure1: "1 1/2 oz",
    strMeasure2: "1/2 oz",
    strMeasure3: "1 oz",
    strMeasure4: null,
    strMeasure5: null,
    strMeasure6: null,
    strMeasure7: null,
    strMeasure8: null,
    strMeasure9: null,
    strMeasure10: null,
    strMeasure11: null,
    imagePath: "/images/margarita.png",
    likeCount: 10
};

// MongoDB에 데이터 삽입
db.cocktails.insertOne(cocktail);