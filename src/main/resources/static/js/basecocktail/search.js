document.addEventListener('DOMContentLoaded', function() {
    const searchButton = document.getElementById('searchButton');
    const ingredientInput = document.getElementById('ingredientInput');
    const glassInput = document.getElementById('glassInput');
    const categoryInput = document.getElementById('categoryInput');

    // searchButton이 클릭되면 실행
    if (searchButton) {
        searchButton.addEventListener('click', function() {
            // 복합 검색 URL 생성
            const ingredient = ingredientInput.value.trim();
            const glass = glassInput.value.trim();
            const category = categoryInput.value.trim();

            let url = '/cocktails/complex';
            let params = [];

            if (ingredient) params.push(`ingredient=${encodeURIComponent(ingredient)}`);
            if (glass) params.push(`glass=${encodeURIComponent(glass)}`);
            if (category) params.push(`type=${encodeURIComponent(category)}`);

            if (params.length > 0) {
                url += '?' + params.join('&');
                sessionStorage.setItem('lastSearchUrl', url)
            }
        });
    }
});