document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.getElementById('toggleSearch');
    const searchOptions = document.getElementById('searchOptions');
    const searchGuide = document.getElementById('search-guide');
    const cocktailButtons = document.querySelector('.cocktail-buttons');

    // 페이지 로드 시 초기 상태 설정
    if (searchOptions.style.display === 'none') {
        cocktailButtons.classList.remove('search-hidden');
    }

    // 검색 버튼 토글
    if (toggleButton && searchOptions && searchGuide && cocktailButtons) {
        toggleButton.addEventListener('click', function() {
            if (searchOptions.style.display === 'none') {
                searchOptions.style.display = 'block';
                searchGuide.style.display = 'block';
                toggleButton.textContent = '검색 옵션 숨기기';
                cocktailButtons.classList.add('search-hidden');
            } else {
                searchOptions.style.display = 'none';
                searchGuide.style.display = 'none';
                toggleButton.textContent = '검색 옵션 토글';
                cocktailButtons.classList.remove('search-hidden');
            }
        });
    }
});