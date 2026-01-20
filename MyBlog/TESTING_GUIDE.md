# Testing Guide - BlogHub

## Pre-Testing Checklist

Before testing, ensure:
- [ ] Project built successfully (`mvn clean package`)
- [ ] Tomcat server is running
- [ ] Application deployed to `http://localhost:8080/bloghub/`
- [ ] Browser console open (F12) for debugging

## Test Scenarios

### 1. User Registration

**Test Case 1.1: Successful Registration**

Steps:
1. Navigate to `http://localhost:8080/bloghub/`
2. Click "Register" button
3. Fill in the form:
   - Username: `testuser1`
   - Email: `test1@example.com`
   - Full Name: `Test User One`
   - Password: `password123`
4. Click "Register"

Expected Result:
- ✅ Success notification appears
- ✅ Modal closes
- ✅ Login modal opens automatically
- ✅ User data saved in database

**Test Case 1.2: Duplicate Username**

Steps:
1. Try to register with same username again
2. Use different email

Expected Result:
- ❌ Error message: "Username already exists"
- ❌ Registration fails

**Test Case 1.3: Form Validation**

Steps:
1. Try to submit empty form
2. Try invalid email format

Expected Result:
- ❌ Browser validation prevents submission
- ❌ Required field warnings

### 2. User Login

**Test Case 2.1: Successful Login**

Steps:
1. Click "Login" button
2. Enter credentials:
   - Username: `testuser1`
   - Password: `password123`
3. Click "Login"

Expected Result:
- ✅ Success notification
- ✅ Page reloads
- ✅ Navigation shows: "Create Post", "My Profile", "Logout"
- ✅ "Login" and "Register" buttons hidden

**Test Case 2.2: Invalid Credentials**

Steps:
1. Try to login with wrong password

Expected Result:
- ❌ Error message: "Invalid credentials"
- ❌ Login fails

**Test Case 2.3: Session Persistence**

Steps:
1. Login successfully
2. Refresh page
3. Navigate to different pages

Expected Result:
- ✅ User remains logged in
- ✅ Session persists across pages

### 3. Create Post

**Test Case 3.1: Create Post (Logged In)**

Steps:
1. Ensure logged in
2. Click "Create Post"
3. Fill in:
   - Title: `My First Blog Post`
   - Content: `This is the content of my first blog post. It's about testing the blog platform.`
4. Click "Publish Post"

Expected Result:
- ✅ Success notification
- ✅ Redirects to post detail page
- ✅ Post displays correctly
- ✅ Author name shows correctly
- ✅ View count starts at 1

**Test Case 3.2: Create Post (Not Logged In)**

Steps:
1. Logout
2. Try to access `create-post.html` directly

Expected Result:
- ✅ Redirects to home page
- ❌ Cannot access create post page

**Test Case 3.3: Empty Post**

Steps:
1. Try to submit empty title or content

Expected Result:
- ❌ Browser validation prevents submission

### 4. View Posts

**Test Case 4.1: View All Posts**

Steps:
1. Navigate to home page
2. Observe posts grid

Expected Result:
- ✅ All posts displayed in grid
- ✅ Shows title, author, date, excerpt
- ✅ Shows view count
- ✅ Posts ordered by newest first

**Test Case 4.2: View Single Post**

Steps:
1. Click on any post card
2. Observe post detail page

Expected Result:
- ✅ Full post content displayed
- ✅ Title, author, date visible
- ✅ View count increments
- ✅ Comments section visible

**Test Case 4.3: View Count Increment**

Steps:
1. Note current view count
2. Refresh page
3. Check view count again

Expected Result:
- ✅ View count increases by 1 each time

### 5. Comments

**Test Case 5.1: Add Comment (Logged In)**

Steps:
1. Login
2. Open any post
3. Scroll to comments section
4. Enter comment: `Great post! Very informative.`
5. Click "Post Comment"

Expected Result:
- ✅ Comment appears immediately
- ✅ Shows username and timestamp
- ✅ Comment form clears

**Test Case 5.2: Add Comment (Not Logged In)**

Steps:
1. Logout
2. Open any post
3. Check comments section

Expected Result:
- ❌ Comment form not visible
- ✅ Can still read existing comments

**Test Case 5.3: Multiple Comments**

Steps:
1. Add 3-5 comments to same post
2. Refresh page

Expected Result:
- ✅ All comments display
- ✅ Ordered chronologically (oldest first)
- ✅ Each shows correct author and time

### 6. User Profile

**Test Case 6.1: View Profile**

Steps:
1. Login
2. Click "My Profile"

Expected Result:
- ✅ Profile page loads
- ✅ Shows username, email
- ✅ Shows bio (or "No bio yet")
- ✅ Lists user's posts

**Test Case 6.2: Edit Profile**

Steps:
1. On profile page, click "Edit Profile"
2. Update:
   - Email: `newemail@example.com`
   - Full Name: `Updated Name`
   - Bio: `This is my bio. I love blogging!`
3. Click "Update Profile"

Expected Result:
- ✅ Success notification
- ✅ Page reloads
- ✅ Changes reflected immediately

**Test Case 6.3: View User's Posts**

Steps:
1. Create 2-3 posts
2. Go to profile page
3. Check "My Posts" section

Expected Result:
- ✅ All user's posts displayed
- ✅ Can click to view each post
- ✅ Shows post stats (views)

### 7. Edit/Delete Posts

**Test Case 7.1: Edit Own Post**

Steps:
1. Login as post author
2. Open your post
3. Click "Edit" button
4. Modify content
5. Save changes

Expected Result:
- ✅ Edit button visible (only for own posts)
- ✅ Changes saved
- ✅ Updated timestamp changes

**Test Case 7.2: Delete Own Post**

Steps:
1. Open your post
2. Click "Delete" button
3. Confirm deletion

Expected Result:
- ✅ Confirmation dialog appears
- ✅ Post deleted from database
- ✅ Redirects to home page
- ✅ Post no longer visible

**Test Case 7.3: Cannot Edit Others' Posts**

Steps:
1. Login as different user
2. Open another user's post

Expected Result:
- ❌ Edit/Delete buttons not visible
- ❌ Cannot modify others' posts

### 8. Logout

**Test Case 8.1: Logout**

Steps:
1. While logged in, click "Logout"

Expected Result:
- ✅ Redirects to home page
- ✅ Navigation shows "Login" and "Register"
- ✅ "Create Post", "My Profile" hidden
- ✅ Session invalidated

**Test Case 8.2: Access Protected Pages After Logout**

Steps:
1. Logout
2. Try to access `create-post.html`
3. Try to access `profile.html`

Expected Result:
- ✅ Redirects to home page
- ❌ Cannot access protected pages

### 9. Responsive Design

**Test Case 9.1: Mobile View**

Steps:
1. Open browser DevTools (F12)
2. Toggle device toolbar
3. Select mobile device (iPhone, Android)
4. Navigate through pages

Expected Result:
- ✅ Layout adapts to mobile
- ✅ Navigation stacks vertically
- ✅ Posts grid becomes single column
- ✅ All features work on mobile

**Test Case 9.2: Tablet View**

Steps:
1. Select tablet device (iPad)
2. Test all features

Expected Result:
- ✅ Layout optimized for tablet
- ✅ All features accessible

### 10. Error Handling

**Test Case 10.1: Network Error**

Steps:
1. Stop Tomcat server
2. Try to perform any action

Expected Result:
- ❌ Error notification appears
- ✅ Application doesn't crash

**Test Case 10.2: Invalid Post ID**

Steps:
1. Navigate to `post-detail.html?id=99999`

Expected Result:
- ❌ Error message or redirect
- ✅ Graceful handling

**Test Case 10.3: Session Timeout**

Steps:
1. Login
2. Wait 30+ minutes (or change timeout in web.xml)
3. Try to create post

Expected Result:
- ❌ Unauthorized error
- ✅ Redirects to login

## Performance Testing

### Test Case P1: Load Time

Steps:
1. Open browser DevTools → Network tab
2. Load home page
3. Check load time

Expected Result:
- ✅ Page loads in < 2 seconds
- ✅ All resources load successfully

### Test Case P2: Multiple Posts

Steps:
1. Create 20+ posts
2. Load home page
3. Check performance

Expected Result:
- ✅ Page loads smoothly
- ✅ No lag or freezing

### Test Case P3: Database Performance

Steps:
1. Create 100+ posts
2. Add 50+ comments
3. Test various operations

Expected Result:
- ✅ Queries execute quickly
- ✅ No noticeable slowdown

## Security Testing

### Test Case S1: SQL Injection

Steps:
1. Try to inject SQL in username field:
   - `admin' OR '1'='1`
2. Try in post content

Expected Result:
- ✅ Input treated as literal string
- ✅ No SQL injection possible

### Test Case S2: XSS (Cross-Site Scripting)

Steps:
1. Try to inject script in post content:
   - `<script>alert('XSS')</script>`
2. Submit and view post

Expected Result:
- ✅ Script not executed
- ✅ Displayed as text

### Test Case S3: Session Hijacking

Steps:
1. Login
2. Copy session cookie
3. Try to use in different browser

Expected Result:
- ✅ Session tied to browser
- ✅ Cannot easily hijack

### Test Case S4: Password Security

Steps:
1. Register user
2. Check database
3. Verify password storage

Expected Result:
- ✅ Password hashed (not plain text)
- ✅ SHA-256 hash visible in database

## Browser Compatibility

Test on:
- [ ] Chrome (latest)
- [ ] Firefox (latest)
- [ ] Edge (latest)
- [ ] Safari (if on Mac)

Expected Result:
- ✅ Works on all modern browsers
- ✅ Consistent appearance
- ✅ All features functional

## Database Testing

### Test Case D1: Database Creation

Steps:
1. Delete `blog.db` file
2. Restart Tomcat
3. Access application

Expected Result:
- ✅ Database auto-created
- ✅ Tables initialized
- ✅ Application works normally

### Test Case D2: Data Persistence

Steps:
1. Create posts and comments
2. Restart Tomcat
3. Check if data persists

Expected Result:
- ✅ All data remains
- ✅ No data loss

### Test Case D3: Foreign Key Constraints

Steps:
1. Check database relationships
2. Try to delete user with posts

Expected Result:
- ✅ Relationships maintained
- ✅ Data integrity preserved

## Automated Testing Script

Create a simple test script:

```javascript
// test-script.js
async function runTests() {
    console.log('Starting automated tests...');
    
    // Test 1: Register
    const registerResult = await fetch('http://localhost:8080/bloghub/register', {
        method: 'POST',
        body: new FormData(document.createElement('form'))
    });
    console.log('Register test:', registerResult.ok ? 'PASS' : 'FAIL');
    
    // Add more tests...
}
```

## Bug Report Template

When you find a bug, document it:

```
Bug ID: #001
Title: [Brief description]
Severity: Critical / High / Medium / Low
Steps to Reproduce:
1. 
2. 
3. 
Expected Result:
Actual Result:
Browser: 
Screenshots: 
```

## Test Results Checklist

After completing all tests:

- [ ] All registration tests pass
- [ ] All login tests pass
- [ ] All post creation tests pass
- [ ] All comment tests pass
- [ ] All profile tests pass
- [ ] All security tests pass
- [ ] All responsive design tests pass
- [ ] All error handling tests pass
- [ ] Performance is acceptable
- [ ] Works on all browsers

## Common Issues & Solutions

**Issue:** Posts not loading
- Check Tomcat logs
- Verify database exists
- Check browser console for errors

**Issue:** Cannot login
- Verify user exists in database
- Check password hashing
- Clear browser cookies

**Issue:** Comments not appearing
- Check if logged in
- Verify post ID is correct
- Check database for comment records

**Issue:** Styling broken
- Clear browser cache
- Check CSS file path
- Verify CSS file deployed

## Next Steps After Testing

1. ✅ Fix any bugs found
2. ✅ Document known issues
3. ✅ Optimize performance bottlenecks
4. ✅ Add more features
5. ✅ Deploy to production

Happy Testing! 🧪
