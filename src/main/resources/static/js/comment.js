function toggleReplyForm(id) {
    const form = document.getElementById("replyForm" + id);
    if (form) {
        form.style.display = (form.style.display === "none" || form.style.display === "") ? "block" : "none";
    }
}

function toggleReplies(commentId) {
    const replyList = document.getElementById('replyList' + commentId);
    const btn = document.getElementById('toggleBtn' + commentId);
    const expanded = replyList.classList.toggle('expanded');
    replyList.classList.toggle('collapsed', !expanded);
    btn.textContent = expanded ? '收起回复' : '展开更多回复';
}

function likeComment(commentId) {
    fetch('/like', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'  // 因为你使用了表单参数
        },
        body: 'comment_id=' + encodeURIComponent(commentId)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('点赞失败');
            }
            return response.json();
        })
        .then(data => {
            // 获取返回的支持数并更新 DOM
            const countElement = document.getElementById('like-count-' + commentId);
            if (countElement && data.support_count !== undefined) {
                countElement.textContent = data.support_count;
                const icon = document.getElementById('like-' + commentId);
                icon.style.filter = 'grayscale(0%)'; // 彩色      // 可选：加点圆角美化
                // 可选：加点圆角美化
            }
        })
        .catch(error => {
            console.error('发生错误:', error);
        });
}
