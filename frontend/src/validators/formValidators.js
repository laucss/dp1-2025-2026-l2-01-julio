export const formValidators = {
    notEmptyValidator: {
        validate: (value) => {
            return value.trim().length > 0;
        },
        message: "The field cannot be empty"
    },
    telephoneValidator: {
        validate: (value) => {
            return value.trim().length === 9 && /^\d+$/.test(value);
        },
        message: "The telephone number must be 9 digits long and contain only numbers"
    },
    notNoneTypeValidator: {
        validate: (value) => {
            return value !== "None";
        },
        message: "Please, select a type"
    },
    validAgeValidator: {
        validate: (value) => {
            return value > 0 && Number.isInteger(Number(value)) && value < 100;
        },
        message: "The age must be a valid number between 1 and 99"
    },
    emailValidator: {
    validate: (value) => {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim());
    },
    message: "Please enter a valid email address"
    }
}