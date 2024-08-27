document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');

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
            } else {
                // 복합 검색 URL 생성
                const ingredient = document.getElementById('ingredientInput').value.trim();
                const glass = document.getElementById('glassInput').value.trim();
                const category = document.getElementById('categoryInput').value.trim();

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

                event.preventDefault();
                window.location.href = url;
            }
        });
    }
});