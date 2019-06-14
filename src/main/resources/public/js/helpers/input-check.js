function input_check_init() {

    function requirements_satisfied(psw) {

        let satisfied = true;
        let psw_problem = "";
        let regex = ""


        regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!regex.test($('#register-email').val())) {
            satisfied = false;
            psw_problem += '* email address is not correct<br>';
        }

        if ($('#register-last-name').val().length === 0 || $('#register-first-name').val().length === 0) {
            satisfied = false;
            psw_problem += '* type your first and last name<br>';
        }

        if (psw.length > 32) {
            satisfied = false;
            psw_problem += '* password cannot be longer than 32 characters<br>';
        }
        else {
            if (psw.length < 10) {
                satisfied = false;
                psw_problem += '* password must have least 10 characters<br>';
            }

            regex = /[a-z]/;
            if (!regex.test(psw)) {
                satisfied = false;
                psw_problem += '* add lower case letter<br>';
            }

            regex = /[A-Z]/;
            if (!regex.test(psw)) {
                satisfied = false;
                psw_problem += '* add upper case letter<br>';
            }

            regex = /[0-9]/;
            if (!regex.test(psw)) {
                satisfied = false;
                psw_problem += '* add number<br>';
            }

            regex = /\W/;   //special characters
            if (!regex.test(psw)) {
                satisfied = false;
                psw_problem += '* add special symbol<br>';
            }

            //spaces and tabulators
            if (psw.includes(" ") || psw.includes("\t")) {
                satisfied = false;
                psw_problem += '* remove whitespace character<br>';
            }

            for (let i = 1; i < psw.length; ++i) {
                if (psw.charAt(i - 1) === psw.charAt(i)) {
                    satisfied = false;
                    psw_problem += '* same character cannot be next to each other<br>';
                    break;
                }
            }

            regex = /@/;
            if (regex.test($('#register-email').val())) {
                let email_name = $('#register-email').val().split("@");
                if (email_name[0].length > 0 && psw.includes(email_name[0])) {
                    satisfied = false;
                    psw_problem += '* password cannot contain your email name<br>';
                }
            }


            if ($('#register-first-name').val().length > 0 && psw.includes($('#register-first-name').val())) {
                satisfied = false;
                psw_problem += '* password cannot contain your first name<br>';
            }

            if ($('#register-last-name').val().length > 0 && psw.includes($('#register-last-name').val())) {
                satisfied = false;
                psw_problem += '* password cannot contain your first name<br>';
            }

            let confirm_psw = $('#register-confirmpassword').val();
            if (confirm_psw.length === 0 || psw.valueOf() !== confirm_psw.valueOf()) {
                satisfied = false;
                psw_problem += '* password and password confirmation are not the same<br>';
            }


        }


        //tooltip update
        $('#password-problem').tooltip('dispose');
        $('#password-problem').attr('title', psw_problem);
        $('#password-problem').removeAttr('data-original-title');
        $('#password-problem').tooltip();

        return satisfied;
    }


    $('.movable').on('input', function () {


            const psw = $('#register-password').val();


            if (psw.length > 0) {
                let score = 0;

                if (requirements_satisfied(psw) === true) {

                    $('#password-problem').addClass('d-none');
                    const result = zxcvbn(psw);
                    score = result.score;
                    $('#submit-btn').prop('disabled', false);
                }
                else {
                    $('#submit-btn').prop('disabled', true);
                    $('#password-problem').removeClass('d-none');
                }

                let add_class = "";
                switch (score) {
                    case 0:
                        add_class = "bg-dark";
                        break;
                    case 1:
                        add_class = "bg-danger";
                        break;
                    case 2:
                        add_class = "bg-warning";
                        break;
                    case 3:
                        add_class = "bg-info";
                        break;
                    case 4:
                        add_class = "bg-success";
                        break;
                }
                const remove_class = "bg-primary bg-success bg-info bg-warning bg-danger bg-secondary bg-dark bg-light";
                $('#password-meter-bar').removeClass(remove_class).addClass(add_class);
                score = (score + 1) * 20;
                const str = score.toString() + "%";
                $('#password-meter-bar').css("width", str);
            }
            else {
                $('#submit-btn').prop('disabled', true);
                $('#password-problem').addClass('d-none');
                $('#password-meter-bar').css("width", "0%");
            }
        }
    );
}