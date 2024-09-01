document.addEventListener('DOMContentLoaded', function() {
    const backToSearchButton = document.getElementById('backToSearchResult');
    const lastSearchUrl = sessionStorage.getItem('lastSearchUrl');

    // 페이지 로드시 적용, cocktailDetail에서 동작
    // 검색 기록이 있다면, 검색화면으로 돌아가는 버튼 활성화
    if (lastSearchUrl.includes('/cocktails/complex')) {
        backToSearchButton.style.display = 'block';
        backToSearchButton.href = lastSearchUrl;
    }
});