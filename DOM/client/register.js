
const mailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
const vm = new Vue({
  el: '#app',
  data: {
    isRegistered:true,
    login:'',
    email:'',
    password:'',
    passwordRep:'',

    emptyLogin:false,
    emptyPass:false,
    emptyEmail:false,
    emailBroken:false,
    passIsNotSame:false,
    passIsShort:false,
  },
  mounted () {
    this.isRegistered = true;
  },
  methods: {
    registerUser(log,pass,mail,passRep){
      this.emptyLogin = false;
      this.emptyPass = false;
      this.emptyEmail = false;
      this.passIsNotSame = false;
      this.passIsShort = false;
      this.emailBroken = false;
      this.validate();
      if(this.emptyLogin === false && this.emptyPass === false && this.emptyEmail === false && this.passIsNotSame === false && this.passIsShort === false && this.emailBroken === false){
      axios.post('http://localhost:8080/api/register',{login:log,password:pass,email:mail}).then((response) =>{
        console.log(response.data);
        console.log(mail)
        if(response.data.code == 200){
          this.isLogged=true;
          sessionStorage.setItem('RegisteredEmail', log);
          window.location.href = 'login.html';
        } else{
          this.isRegistered=false;

        }

      }).catch( error => { console.log(error); });
    }
    },
    validate(){
      if(this.login === '') this.emptyLogin = true;
      if(this.password === '') this.emptyPass = true;
      if(this.email === '') this.emptyEmail = true;
      if(this.password != this.passwordRep) this.passIsNotSame = true;
      if(this.password.length < 6) this.passIsShort = true;
      if(!mailRegex.test(this.email)) this.emailBroken = true;
      console.log(mailRegex.test(this.email));
    },
    goToLogin(){
      window.location.href = 'login.html';
    }

  }
});
