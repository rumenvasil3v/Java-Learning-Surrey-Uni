// Create post
document.addEventListener('DOMContentLoaded', async () => {
    // Check if user is logged in
    const user = await checkAuth();
    if (!user) {
        window.location.href = 'index.html';
        return;
    }
    
    const createPostForm = document.getElementById('createPostForm');
    if (createPostForm) {
        createPostForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(createPostForm);
            const data = Object.fromEntries(formData);
            
            try {
                const result = await API.post('posts/', data);
                if (result.id) {
                    showNotification('Post created successfully!', 'success');
                    setTimeout(() => {
                        window.location.href = `post-detail.html?id=${result.id}`;
                    }, 1000);
                } else {
                    showNotification('Failed to create post', 'error');
                }
            } catch (error) {
                console.error('Error creating post:', error);
                showNotification('Failed to create post', 'error');
            }
        });
    }
});
