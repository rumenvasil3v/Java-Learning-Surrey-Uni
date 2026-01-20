// Check authentication status
async function checkAuth() {
    try {
        const response = await fetch('user/current');
        if (response.ok) {
            const user = await response.json();
            updateNavForLoggedIn();
            return user;
        } else {
            updateNavForLoggedOut();
            return null;
        }
    } catch (error) {
        updateNavForLoggedOut();
        return null;
    }
}

function updateNavForLoggedIn() {
    const loginBtn = document.getElementById('loginBtn');
    const registerBtn = document.getElementById('registerBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const createPostLink = document.getElementById('createPostLink');
    const profileLink = document.getElementById('profileLink');
    
    if (loginBtn) loginBtn.style.display = 'none';
    if (registerBtn) registerBtn.style.display = 'none';
    if (logoutBtn) logoutBtn.style.display = 'block';
    if (createPostLink) createPostLink.style.display = 'block';
    if (profileLink) profileLink.style.display = 'block';
}

function updateNavForLoggedOut() {
    const loginBtn = document.getElementById('loginBtn');
    const registerBtn = document.getElementById('registerBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const createPostLink = document.getElementById('createPostLink');
    const profileLink = document.getElementById('profileLink');
    
    if (loginBtn) loginBtn.style.display = 'block';
    if (registerBtn) registerBtn.style.display = 'block';
    if (logoutBtn) logoutBtn.style.display = 'none';
    if (createPostLink) createPostLink.style.display = 'none';
    if (profileLink) profileLink.style.display = 'none';
}

// Initialize auth on page load
document.addEventListener('DOMContentLoaded', async () => {
    await checkAuth();
    
    // Load posts
    if (document.getElementById('postsGrid')) {
        loadPosts();
    }
    
    // Login button
    const loginBtn = document.getElementById('loginBtn');
    if (loginBtn) {
        loginBtn.addEventListener('click', (e) => {
            e.preventDefault();
            openModal('loginModal');
        });
    }
    
    // Register button
    const registerBtn = document.getElementById('registerBtn');
    if (registerBtn) {
        registerBtn.addEventListener('click', (e) => {
            e.preventDefault();
            openModal('registerModal');
        });
    }
    
    // Logout button
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', async (e) => {
            e.preventDefault();
            window.location.href = 'logout';
        });
    }
    
    // Switch between login and register
    const switchToRegister = document.getElementById('switchToRegister');
    if (switchToRegister) {
        switchToRegister.addEventListener('click', (e) => {
            e.preventDefault();
            closeModal('loginModal');
            openModal('registerModal');
        });
    }
    
    const switchToLogin = document.getElementById('switchToLogin');
    if (switchToLogin) {
        switchToLogin.addEventListener('click', (e) => {
            e.preventDefault();
            closeModal('registerModal');
            openModal('loginModal');
        });
    }
    
    // Login form
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(loginForm);
            const data = Object.fromEntries(formData);
            
            try {
                const result = await API.post('login', data);
                if (result.success) {
                    showNotification('Login successful!', 'success');
                    closeModal('loginModal');
                    window.location.reload();
                } else {
                    showNotification(result.message, 'error');
                }
            } catch (error) {
                showNotification('Login failed', 'error');
            }
        });
    }
    
    // Register form
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(registerForm);
            const data = Object.fromEntries(formData);
            
            try {
                const result = await API.post('register', data);
                if (result.success) {
                    showNotification('Registration successful! Please login.', 'success');
                    closeModal('registerModal');
                    openModal('loginModal');
                } else {
                    showNotification(result.message, 'error');
                }
            } catch (error) {
                showNotification('Registration failed', 'error');
            }
        });
    }
});

// Load posts
async function loadPosts() {
    try {
        const posts = await API.get('posts/');
        const postsGrid = document.getElementById('postsGrid');
        
        if (posts.length === 0) {
            postsGrid.innerHTML = '<p style="text-align:center; color:#7f8c8d;">No posts yet. Be the first to create one!</p>';
            return;
        }
        
        postsGrid.innerHTML = posts.map(post => `
            <div class="post-card" onclick="window.location.href='post-detail.html?id=${post.id}'">
                <h3>${post.title}</h3>
                <div class="post-meta">
                    By ${post.authorName} on ${formatDate(post.createdAt)}
                </div>
                <div class="post-excerpt">
                    ${truncate(post.content, 200)}
                </div>
                <div class="post-stats">
                    <span>👁️ ${post.views} views</span>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('Failed to load posts:', error);
        showNotification('Failed to load posts', 'error');
    }
}
