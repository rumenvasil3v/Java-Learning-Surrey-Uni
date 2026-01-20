document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const postId = urlParams.get('id');
    
    if (!postId) {
        window.location.href = 'index.html';
        return;
    }
    
    const user = await checkAuth();
    
    // Load post
    try {
        const data = await API.get(`posts/${postId}`);
        const post = data.post;
        const comments = data.comments;
        
        const postDetail = document.getElementById('postDetail');
        postDetail.innerHTML = `
            <h1>${post.title}</h1>
            <div class="post-meta">
                By ${post.authorName} on ${formatDate(post.createdAt)}
                <span style="margin-left: 1rem;">👁️ ${post.views} views</span>
            </div>
            <div class="post-content">${post.content}</div>
            ${user && user.id === post.authorId ? `
                <div class="post-actions">
                    <button onclick="editPost(${post.id})">Edit</button>
                    <button class="danger" onclick="deletePost(${post.id})">Delete</button>
                </div>
            ` : ''}
        `;
        
        // Show comment form if logged in
        if (user) {
            document.getElementById('commentForm').style.display = 'block';
        }
        
        // Load comments
        loadComments(comments);
        
    } catch (error) {
        console.error('Failed to load post:', error);
        showNotification('Failed to load post', 'error');
    }
    
    // Add comment form
    const addCommentForm = document.getElementById('addCommentForm');
    if (addCommentForm) {
        addCommentForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(addCommentForm);
            const data = Object.fromEntries(formData);
            data.postId = postId;
            
            try {
                const comment = await API.post('comments/', data);
                if (comment.id) {
                    showNotification('Comment added!', 'success');
                    addCommentForm.reset();
                    // Reload comments
                    const postData = await API.get(`posts/${postId}`);
                    loadComments(postData.comments);
                }
            } catch (error) {
                showNotification('Failed to add comment', 'error');
            }
        });
    }
});

function loadComments(comments) {
    const commentsList = document.getElementById('commentsList');
    
    if (comments.length === 0) {
        commentsList.innerHTML = '<p style="color:#7f8c8d;">No comments yet. Be the first to comment!</p>';
        return;
    }
    
    commentsList.innerHTML = comments.map(comment => `
        <div class="comment">
            <div class="comment-author">${comment.username}</div>
            <div class="comment-date">${formatDate(comment.createdAt)}</div>
            <div class="comment-content">${comment.content}</div>
        </div>
    `).join('');
}

async function deletePost(postId) {
    if (!confirm('Are you sure you want to delete this post?')) return;
    
    try {
        await API.delete(`posts/${postId}`);
        showNotification('Post deleted successfully', 'success');
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1000);
    } catch (error) {
        showNotification('Failed to delete post', 'error');
    }
}

function editPost(postId) {
    // For simplicity, redirect to create page with edit mode
    showNotification('Edit feature: Implement edit form or redirect to edit page', 'info');
}
