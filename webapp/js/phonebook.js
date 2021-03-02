function Contact(firstName, lastName, phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.checked = false;
    this.shown = true;
}

new Vue({
    el: "#app",
    data: {
        validation: false,
        serverValidation: false,
        firstName: "",
        lastName: "",
        phone: "",
        rows: [],
        serverError: "",
        filterText: ""
    },

    methods: {
        deleteContact: function (contact) {
            var self = this;

            $.ajax({
                type: "POST",
                url: "/phonebook/delete",
                data: JSON.stringify(contact)
            }).done(function () {
                self.serverValidation = false;
                self.loadData();
            }).fail(function (ajaxRequest) {
                var contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            });
        },

        deleteCheckedContacts: function () {
            var chekedContacts = this.rows.filter(function (row) {
                return row.checked;
            });

            if (chekedContacts.length === 0) {
                this.serverValidation = true;
                this.serverError = "Не выбрано ни одного контакта для удаления";
                return;
            }

            var self = this;

            $.ajax({
                type: "POST",
                url: "/phonebook/deleteChecked",
                data: JSON.stringify(chekedContacts)
            }).done(function () {
                self.serverValidation = false;
                self.loadData();
            }).fail(function (ajaxRequest) {
                var contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            });
        },

        applyFilter: function () {
            var self = this;

            $.get("/phonebook/get/filtered", {term: this.filterText}).done(function (response) {
                var filteredContacts = self.convertContactList(JSON.parse(response));

                filteredContacts.forEach(function (contact) {
                    self.rows.forEach(function (row) {
                        if (row.id === contact.id && row.checked) {
                            contact.checked = true;
                        }
                    });
                });

                self.rows = filteredContacts;
                self.serverValidation = false;
            }).fail(function () {
                self.serverError = "Не удалось получить список отфильтрованных контактов с сервера";
                self.serverValidation = true;
            });
        },

        resetFilter: function () {
            this.filterText = "";
            this.loadData();
        },

        contactToString: function (contact) {
            var note = "(";
            note += contact.firstName + ", ";
            note += contact.lastName + ", ";
            note += contact.phone;
            note += ")";
            return note;
        },

        convertContactList: function (contactListFromServer) {
            return contactListFromServer.map(function (contact, i) {
                return {
                    id: contact.id,
                    firstName: contact.firstName,
                    lastName: contact.lastName,
                    phone: contact.phone,
                    checked: false,
                    shown: true,
                    number: i + 1
                };
            });
        },

        addContact: function () {
            if (this.hasError) {
                this.validation = true;
                this.serverValidation = false;
                return;
            }

            var self = this;

            var contact = new Contact(this.firstName, this.lastName, this.phone);
            $.ajax({
                type: "POST",
                url: "/phonebook/add",
                data: JSON.stringify(contact)
            }).done(function () {
                self.serverValidation = false;
                self.loadData();
            }).fail(function (ajaxRequest) {
                var contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            });

            self.firstName = "";
            self.lastName = "";
            self.phone = "";
            self.validation = false;
        },

        loadData: function () {
            var self = this;

            $.get("/phonebook/get/all").done(function (response) {
                var contactListFormServer = JSON.parse(response);
                self.rows = self.convertContactList(contactListFormServer);
                self.serverValidation = false;
            }).fail(function () {
                self.serverError = "Не удалось получить список контактов с сервера";
                self.serverValidation = true;
            });
        }
    },

    computed: {
        firstNameError: function () {
            if (this.firstName) {
                return {
                    message: "",
                    error: false
                };
            }

            return {
                message: "Поле Имя должно быть заполнено.",
                error: true
            };
        },

        lastNameError: function () {
            if (!this.lastName) {
                return {
                    message: "Поле Фамилия должно быть заполнено.",
                    error: true
                };
            }

            return {
                message: "",
                error: false
            };
        },

        phoneError: function () {
            if (!this.phone) {
                return {
                    message: "Поле Телефон должно быть заполнено.",
                    error: true
                };
            }

            var self = this;

            var sameContact = this.rows.some(function (c) {
                return c.phone === self.phone;
            });

            if (sameContact) {
                return {
                    message: "Номер телефона не должен дублировать другие номера в телефонной книге.",
                    error: true
                };
            }

            return {
                message: "",
                error: false
            };
        },

        hasError: function () {
            return this.lastNameError.error || this.firstNameError.error || this.phoneError.error;
        }
    },

    created: function () {
        this.loadData();
    }
});

