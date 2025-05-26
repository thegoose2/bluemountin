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