document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.getElementById('toggleSearch');
    const searchOptions = document.getElementById('searchOptions');
    const searchButton = document.getElementById('searchButton');
    const ingredientInput = document.getElementById('ingredientInput');
    const glassInput = document.getElementById('glassInput');
    const categoryInput = document.getElementById('categoryInput');

    if (toggleButton && searchOptions) {
        toggleButton.addEventListener('click', function() {
            if (searchOptions.style.display === 'none') {
                searchOptions.style.display = 'block';
                toggleButton.textContent = '검색 옵션 숨기기';
            } else {
                searchOptions.style.display = 'none';
                toggleButton.textContent = '검색 옵션 토글';
            }
        });
    }

    if (searchButton) {
        searchButton.addEventListener('click', function() {
            const ingredient = ingredientInput.value.trim();
            const glass = glassInput.value.trim();
            const category = categoryInput.value.trim();

            let url = '/cocktails/all';
            let params = [];

            if (ingredient) params.push(`ingredient=${encodeURIComponent(ingredient)}`);
            if (glass) params.push(`glass=${encodeURIComponent(glass)}`);
            if (category) params.push(`type=${encodeURIComponent(category)}`);

            if (params.length === 1) {
                if (ingredient) url += '/ingredient';
                else if (glass) url += '/glass';
                else if (category) url += '/type';
            } else if (params.length > 1) {
                url += '/complex';
            }

            if (params.length > 0) {
                url += '?' + params.join('&');
            }

            window.location.href = url;
        });
    }
});