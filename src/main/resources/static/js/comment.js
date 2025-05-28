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

function followUser(followee_id) {
    fetch(`/follow/${followee_id}`, {
        method: "POST"
    })
        .then(response => {
            if (response.ok) {
                // 成功后更新按钮 UI
                document.getElementById("follow-btn").style.display = "none";
                document.getElementById("unfollow-btn").style.display = "inline-block";
            }
        });
}

function unfollowUser(followee_id) {
    fetch(`/follow/${followee_id}`, {
        method: "DELETE"
    })
        .then(response => {
            if (response.ok) {
                document.getElementById("follow-btn").style.display = "inline-block";
                document.getElementById("unfollow-btn").style.display = "none";
            }
        });
}

//选择收藏夹界面
function openFavoriteModal(articleId) {
    document.getElementById('favorite-modal').style.display = 'block';
    document.getElementById('favorite-article-id').value = articleId;
}

function closeFavoriteModal() {
    document.getElementById('favorite-modal').style.display = 'none';
}

function submitFavorite() {
    const articleId = document.getElementById('favorite-article-id').value;
    const folderId = document.querySelector('input[name="folder"]:checked')?.value;

    if (!folderId) {
        alert("请选择一个收藏夹！");
        return;
    }

    fetch(`/favorite/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            articleId: articleId,
            folderId: folderId
        })
    })
        .then(response => {
            if (response.ok) {
                alert("收藏成功！");
                closeFavoriteModal();
            } else {
                alert("收藏失败！");
            }
        });
}
//选择收藏夹界面

//私信
function openChat() {
    document.getElementById("chat-dialog").style.display = "flex";
}

function closeChat() {
    document.getElementById("chat-dialog").style.display = "none";
}

function sendMessage() {
    const input = document.getElementById("message-input");
    const message = input.value.trim();
    if (message) {
        const chatBody = document.getElementById("chat-body");

        // 生成我方消息气泡
        const msgDiv = document.createElement("div");
        msgDiv.className = "message right";
        msgDiv.innerHTML = `<div class="bubble">${message}</div>`;
        chatBody.appendChild(msgDiv);

        input.value = "";
        chatBody.scrollTop = chatBody.scrollHeight; // 滚动到底部
    }
}

//私信