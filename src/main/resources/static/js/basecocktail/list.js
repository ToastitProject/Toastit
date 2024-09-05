document.addEventListener('DOMContentLoaded', function() {
    const lastSearchUrl = sessionStorage.getItem('lastSearchUrl');
    const currentUrl = window.location.href;

    // 페이지 로드 시 적용, cocktailList에서 동작
    // Session storage에 저장된 lastSearchUrl을 삭제
    if(lastSearchUrl && lastSearchUrl.includes('/cocktails/complex')){
        sessionStorage.removeItem('lastSearchUrl');
    }

    // 현재 URL을 lastListUrl로 저장
    sessionStorage.setItem('lastListUrl', currentUrl);
});
