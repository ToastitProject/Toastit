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

    // 페이지네이션 링크에 이벤트 리스너 추가
    const paginationLinks = document.querySelectorAll('.pagination a');
    paginationLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const newUrl = this.href;
            sessionStorage.setItem('lastListUrl', newUrl);
            window.location.href = newUrl;
        });
    });
});
