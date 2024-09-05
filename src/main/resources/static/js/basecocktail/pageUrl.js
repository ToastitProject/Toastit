document.addEventListener('DOMContentLoaded', function() {

    // 페이지네이션 링크에 이벤트 리스너 추가 - allList
    const basePaginationLinks = document.querySelectorAll('.base-pagination a');
    basePaginationLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const newUrl = this.href;
            sessionStorage.setItem('lastListUrl', newUrl);
            window.location.href = newUrl;
        });
    });

    // 페이지네이션 링크에 이벤트 리스너 추가 - search
    const searchPaginationLinks = document.querySelectorAll('.search-pagination a');
    searchPaginationLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const newUrl = this.href;
            sessionStorage.setItem('lastSearchUrl', newUrl);
            window.location.href = newUrl;
        });
    });
});