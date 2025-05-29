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
function openChat(receiver_id) {
    document.getElementById("chat-dialog").style.display = "flex";
    fetch(`/messages/${receiver_id}`) // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('message-main-content').innerHTML = html;
        });
}

function closeChat() {
    document.getElementById("chat-dialog").style.display = "none";
}

function sendMessage(receiverId) {
    const input = document.getElementById("message-input");
    const content = input.value.trim();
    if (!content) return;

    fetch('/messages/send', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({receiverId, content})
    }).then(res => {
        if (res.ok) {
            input.value = ""; // 清空输入框
            loadMessages(receiverId); // ✅ 重新加载整个对话框消息
        } else {
            alert("发送失败");
        }
    }).catch(err => {
        console.error("发送消息失败", err);
    });
}

function loadMessages(receiverId) {
    fetch(`/messages/${receiverId}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('message-main-content').innerHTML = html;
            // 自动滚动到底部
            const chatBody = document.getElementById("message-main-content");
            chatBody.scrollTop = chatBody.scrollHeight;
        })
        .catch(error => {
            console.error("加载消息失败", error);
        });
}

const chatDialog = document.getElementById('chat-dialog');
const chatHeader = chatDialog.querySelector('.chat-header');

let isDragging = false;
let offsetX = 0;
let offsetY = 0;

chatHeader.addEventListener('mousedown', function (e) {
    isDragging = true;
    offsetX = e.clientX - chatDialog.offsetLeft;
    offsetY = e.clientY - chatDialog.offsetTop;
    document.body.style.userSelect = 'none'; // 防止拖动中选中文本
});

document.addEventListener('mousemove', function (e) {
    if (isDragging) {
        chatDialog.style.left = (e.clientX - offsetX) + 'px';
        chatDialog.style.top = (e.clientY - offsetY) + 'px';
    }
});

document.addEventListener('mouseup', function () {
    isDragging = false;
    document.body.style.userSelect = '';
});
//私信