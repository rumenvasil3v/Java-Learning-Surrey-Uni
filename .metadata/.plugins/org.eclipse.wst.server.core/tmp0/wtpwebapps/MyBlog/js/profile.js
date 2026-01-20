document.addEventListener('DOMContentLoaded', async () => {
    const user = await checkAuth();
    if (!user) {
        window.location.href = 'index.html';
        return;
    }
    
    // Load user profile
    try {
        const userData = await API.get('user/current');
        document.getElementById('profileUsername').textContent = userData.username;
        document.getElementById('profileEmail').textContent = userData.email;
        document.getElementById('profileBio').textContent = userData.bio || 'No bio yet';
        
        // Load user posts
        const posts = await API.get(`posts/user/${userData.id}`);
        const userPosts = document.getElementById('userPosts');
        
        if (posts.length === 0) {
            userPosts.innerHTML = '<p style="text-align:center; color:#7f8c8d;">You haven\'t created any posts yet.</p>';
        } else {
            userPosts.innerHTML = posts.map(post => `
                <div class="post-card" onclick="window.location.href='post-detail.html?id=${post.id}'">
                    <h3>${post.title}</h3>
                    <div class="post-meta">
                        ${formatDate(post.createdAt)}
                    </div>
                    <div class="post-excerpt">
                        ${truncate(post.content, 200)}
                    </div>
                    <div class="post-stats">
                        <span>👁️ ${post.views} views</span>
                    </div>
                </div>
            `).join('');
        }
        
        // Edit profile button
        document.getElementById('editProfileBtn').addEventListener('click', () => {
            openModal('editProfileModal');
            // Pre-fill form
            const form = document.getElementById('editProfileForm');
            form.email.value = userData.email;
            form.fullName.value = userData.fullName;
            form.bio.value = userData.bio || '';
        });
        
        // Edit profile form
        document.getElementById('editProfileForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const data = Object.fromEntries(formData);
            
            try {
                const result = await API.put('user/update', data);
                if (result.id) {
                    showNotification('Profile updated successfully!', 'success');
                    closeModal('editProfileModal');
                    window.location.reload();
                }
            } catch (error) {
                showNotification('Failed to update profile', 'error');
            }
        });
        
    } catch (error) {
        console.error('Failed to load profile:', error);
        showNotification('Failed to load profile', 'error');
    }
});
