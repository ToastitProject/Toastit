document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const lastSearchUrl = sessionStorage.getItem('lastSearchUrl')

    // 폼 제출 이벤트 처리
    if (form) {
        form.addEventListener('submit', function(event) {
            const inputs = form.querySelectorAll('input[type="text"]');
            let isEmpty = true;

            inputs.forEach(function(input) {
                if (input.value.trim() !== '') {
                    isEmpty = false;
                }
            });

            if (isEmpty) {
                event.preventDefault();
                alert('검색하려면 최소 하나의 필드를 입력해야 합니다.');
            }else{
                window.location.href = lastSearchUrl;
            }
        });
    }
});