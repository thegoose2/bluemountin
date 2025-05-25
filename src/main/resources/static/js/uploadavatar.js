document.addEventListener("DOMContentLoaded", function () {
    const avatarInput = document.getElementById('avatarInput');
    const avatarForm = document.getElementById('avatarForm');

    // 点击按钮触发文件选择
    document.getElementById('uploadButton').addEventListener('click', function () {
        avatarInput.click();
    });

    // 选择完文件自动提交
    avatarInput.addEventListener('change', function () {
        avatarForm.submit();
    });
});

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.post-content').forEach(preview => {
        const content = preview.querySelector('.content-snippet');
        const readMore = preview.querySelector('.read-more');

        if (content.scrollHeight > content.clientHeight) {
            readMore.style.display = 'inline-block'; // 只在超出时显示
        }
    });
});

function loadProfile() {
    fetch('/profile/fragment') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('main-content').innerHTML = html;
        });
}

function loadfeed() {
    fetch('/gate/fragment') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('main-content').innerHTML = html;
        });
}

function loadlogin() {
    fetch('/login/fragment') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('login-main').innerHTML = html;
        });
}

function loadregister() {
    fetch('/registerer/fragment') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('login-main').innerHTML = html;
        });
}