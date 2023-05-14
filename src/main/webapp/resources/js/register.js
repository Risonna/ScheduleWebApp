window.onload = function() {
    const inputPlaceholderMap = {
        'loginForm:username': 'Введите имя пользователя',
        'loginForm:password': 'Введите пароль',
        'loginForm:passwordConfirm': 'Подтвердите пароль',
        'loginForm:Email': 'Укажите электронную почту'
    };

    const inputs = document.querySelectorAll("input[type='text'], input[type='password']");
    inputs.forEach(input => {
        const inputId = input.id;
        const placeholderText = inputPlaceholderMap[inputId];

        input.addEventListener("focus", function() {
            this.removeAttribute("placeholder");
        });
        input.addEventListener("blur", function() {
            this.setAttribute("placeholder", placeholderText);
        });

        // Set the placeholder on page load if the field is empty
        if (!input.value && placeholderText) {
            input.setAttribute("placeholder", placeholderText);
        }
    });
};
