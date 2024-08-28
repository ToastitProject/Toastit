document.addEventListener('DOMContentLoaded', function() {
    const backToSearchButton = document.getElementById('backToSearchResult');
    const lastSearchUrl = sessionStorage.getItem('lastSearchUrl');

    if (lastSearchUrl.includes('/cocktails/all/complex')) {
        backToSearchButton.style.display = 'block';
        backToSearchButton.href = lastSearchUrl;
    }
});