const mailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
const vm = new Vue({
  el: '#app',
  data: {
    registerMessage:"",
    name:'',
    login:'',
    email:'',
    password:'',
    passwordRep:'',
    street:'',
    homeNumber:'',
    city:'',
    zipCode:'',
    code:'',

    emptyLogin:false,
    emptyPass:false,
    emptyEmail:false,
    emailBroken:false,
    passIsNotSame:false,
    passIsShort:false,
    emptyStreet:false,
    emptyName:false,
    emptyCity:false,
    emptyHomeNumber:false,
    emptyZipCode:false,
    emptyCode:false,
  },
  mounted () {
    this.registerMessage = "";
  },
  methods: {

    registerStation(name,login,password,street,homeNumber,city,zipCode,code){
      this.emptyLogin=false;
      this.emptyPass=false;
      this.emptyEmail=false;
      this.emailBroken=false;
      this.passIsNotSame=false;
      this.passIsShort=false;
      this.emptyStreet=false;
      this.emptyName=false;
      this.emptyCity=false;
      this.emptyHomeNumber=false;
      this.emptyZipCode=false;
      this.emptyCode=false;

      this.validate();
      if(this.emptyName === false && this.emptyLogin === false && this.emptyPass === false && this.emptyEmail === false && this.passIsNotSame === false && this.passIsShort === false
      && this.emailBroken === false && this.emptyCity === false && this.emptyStreet === false && this.emptyHomeNumber=== false && this.emptyZipCode === false && this.emptyCode === false){
      axios.post('http://localhost:8081/sapi/register',{name:name, login:login,password:password,street:street,homeNumber:homeNumber,city:city,zipCode:zipCode,code:code}).then((response) =>{
        console.log(response.data);
        if(response.data.code == 200){
          localStorage.setItem('RegisteredStationLogin', login);
          window.location.href = 'stationLogin.html';
        }
        if(response.data.code == 403){
          this.registerMessage = "Błędny kod rejestracyjny";
        }
        if(response.data.code == 409){
          this.registerMessage = "Taki login już istnieje";
        }

      }).catch( error => { console.log(error); });
    }
    },
    validate(){
      if(this.name === '') this.emptyName = true;
      if(this.login === '') this.emptyLogin = true;
      if(this.password === '') this.emptyPass = true;
      if(this.email === '') this.emptyEmail = true;
      if(this.street === '') this.emptyStreet = true;
      if(this.city === '') this.emptyCity = true;
      if(this.zipCode === '') this.emptyZipCode = true;
      if(this.homeNumber === '') this.emptyHomeNumber = true;
      if(this.code === '') this.emptyCode = true;
      if(this.password != this.passwordRep) this.passIsNotSame = true;
      if(this.password.length < 6) this.passIsShort = true;
      if(!mailRegex.test(this.email)) this.emailBroken = true;

    },
    goToLogin(){
      window.location.href = 'stationLogin.html';
    }

  }
});
