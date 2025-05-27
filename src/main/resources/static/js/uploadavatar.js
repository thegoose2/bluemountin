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

function loadProfile() {
    fetch('/profile/fragment') // 请求片段
        .then(response => response.text())
        .then(html => {
            // 判断是否拿到的是登录页面（最小入侵式）
            if (html.includes('<form') && html.includes('name="username"')) {
                // 登录页特征，跳转
                window.location.href = '/login';
            } else {
                // 正常加载片段
                document.getElementById('main-content').innerHTML = html;
            }
        });
}

function loadfeed() {
    fetch('/gate/fragment') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('main-content').innerHTML = html;
        });
}

function loadaready() {
    fetch('/myblog?temp=1') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('article-main-content').innerHTML = html;
        });
}

function loadwait() {
    fetch('/myblog?temp=0') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('article-main-content').innerHTML = html;
        });
}

function loadwaiting() {
    fetch('/myblog?temp=2') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('article-main-content').innerHTML = html;
        });
}

function loadregister() {
    fetch('/registerer/fragment') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('login-main').innerHTML = html;
        });
}

function loadlogin() {
    fetch('/login/fragment') // 请求片段
        .then(response => response.text())
        .then(html => {
            document.getElementById('login-main').innerHTML = html;
        });
}

// 保存颜色的对象
const originalBackgrounds = {};

function transformcolor(id) {
    const iconElement = document.getElementById(id);
    if (!originalBackgrounds[id]) {
        // 第一次修改时保存原始颜色
        originalBackgrounds[id] = iconElement.style.backgroundColor || window.getComputedStyle(iconElement).backgroundColor;
    }
    iconElement.style.backgroundColor = '#ffffff'; // 改成白色
}

function restorecolor(id) {
    if (originalBackgrounds[id]) {
        iconElement.style.backgroundColor = originalBackgrounds[id];
    }
}

function likearticle(articleId) {
    fetch('/like/article', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'  // 因为你使用了表单参数
        },
        body: 'article_id=' + encodeURIComponent(articleId)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('点赞失败');
            }
            return response.json();
        })
        .then(data => {
            // 获取返回的支持数并更新 DOM
            const countElement = document.getElementById('article-like-count-' + articleId);
            if (countElement && data.like_count !== undefined) {
                countElement.textContent = data.like_count;
                const icon = document.getElementById('article-like-' + articleId);
                icon.style.filter = 'grayscale(0%)'; // 彩色      // 可选：加点圆角美化
            }
        })
        .catch(error => {
            console.error('发生错误:', error);
            window.location.href = '/login';  // 跳转登录页
        });
}

