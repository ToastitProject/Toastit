document.addEventListener('DOMContentLoaded', function() {
    const backToListButton = document.getElementById('backToList');
    const backToSearchButton = document.getElementById('backToSearchResult');
    const lastSearchUrl = sessionStorage.getItem('lastSearchUrl');
    const lastListUrl = sessionStorage.getItem('lastListUrl');

    // 페이지 로드시 적용, cocktailDetail에서 동작
    // 검색 기록이 있다면, 검색화면으로 돌아가는 버튼 활성화
    if (lastSearchUrl && lastSearchUrl.includes('/cocktails/complex')) {
        backToSearchButton.style.display = 'block';
        backToSearchButton.href = lastSearchUrl;
    }

    // 목록으로 돌아가기
    if (lastListUrl && lastListUrl.includes('/cocktails/all')) {
        backToListButton.href = lastListUrl;
    } else {
        backToListButton.href = '/cocktails/all';
    }
});