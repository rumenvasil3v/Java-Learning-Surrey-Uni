let element = document.getElementById('main-heading');
element.textContent = 'My New Web App';

let button = document.getElementById('change-btn');
button.addEventListener('click', function() {
   element.textContent = 'Clicked!'; 
});