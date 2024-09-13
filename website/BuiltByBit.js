const { Wrapper, Token, TokenType } = require("@builtbybit/api-wrapper");

let token = new Token(TokenType.PRIVATE, "Find @ https://builtbybit.com/account/api");
let wrapper = new Wrapper();

wrapper.init(token).then(wrapper.members().self().then(self => {
	console.log(self);
})).catch(error => {
	console.log("ERROR: " + error);
});