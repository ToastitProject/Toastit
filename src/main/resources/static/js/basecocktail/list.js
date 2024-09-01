document.addEventListener('DOMContentLoaded', function() {
    const lastSearchUrl = sessionStorage.getItem('lastSearchUrl');

    // 페이지 로드 시 적용, cocktailList에서 동작
    // Session storage 에 저장된 lastSearchUrl 을 삭제
    if(lastSearchUrl.includes('/cocktails/complex')){
        sessionStorage.removeItem('lastSearchUrl');
    }
});