document.addEventListener('DOMContentLoaded', function() {
    const cocktailId = document.querySelector('.base-cocktail-number').innerText;
    const likeButton = document.getElementById('likeButton');

    let isLiked = /*[[${isLiked}]]*/ false;

    function likeCocktail() {
        fetch('/baseLike', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({"base-cocktail-number": cocktailId})
        })
            .then(response => {
                if (response.ok) {
                    isLiked = !isLiked;
                    likeButton.innerText = isLiked ? '좋아요 취소' : '좋아요';
                    alert(isLiked ? '좋아요 상태가 반영되었습니다!' : '좋아요가 취소되었습니다!');
                    setTimeout(() => { location.reload(); }, 500);
                } else {
                    alert('좋아요 처리 중 오류가 발생했습니다.');
                    location.reload();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('좋아요는 로그인을 하셔야 반영할 수 있습니다.');
            });
    }

    if (likeButton) {
        likeButton.addEventListener('click', likeCocktail);
    }
});