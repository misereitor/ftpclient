const listCnpj = document.querySelector("div.listcnpj")
const inputCnpj = document.querySelector("input.cnpj")
const buttonAddCnpj = document.querySelector("button.addcnpj")
inputCnpj.addEventListener("keyup", cnpjFormat)
buttonAddCnpj.addEventListener("click", addCnpj)
let cont = 0

function cnpjFormat() {
	inputCnpj.value = inputCnpj.value.replace(/\D/g, '')
	inputCnpj.value = inputCnpj.value.replace(/^(\d{2})(\d)/, '$1.$2')
	inputCnpj.value = inputCnpj.value.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
	inputCnpj.value = inputCnpj.value.replace(/\.(\d{3})(\d)/, '.$1/$2')
	inputCnpj.value = inputCnpj.value.replace(/(\d{4})(\d)/, '$1-$2')
}
function addCnpj(c = "") {
	if (inputCnpj.value.length === 18 || c.length == 18) {
		const divEl = document.createElement("div")
		divEl.className = `cnpj${cont}`
		const pEle = document.createElement("p")
		const buttonRemove = document.createElement("button")
		buttonRemove.innerHTML = "X"
		buttonRemove.setAttribute("onclick", `removeCnpjTheList("cnpj${cont}")`)
		let cnpj = ""
		if (c.length == 18) {
			cnpj = document.createTextNode(c)
		} else {
			cnpj = document.createTextNode(inputCnpj.value)
		}
		divEl.appendChild(pEle)
		divEl.appendChild(buttonRemove)
		pEle.appendChild(cnpj)
		listCnpj.appendChild(divEl)
		cont += 1
		inputCnpj.value = ''
	}
}
function removeCnpjTheList(elem) {
	document.querySelector(`div.${elem}`).remove()
}
function submitRegister() {
	const pElement = document.querySelectorAll("div.listcnpj p")
	const cnpjs = []
	pElement.forEach(el => {
		const e = el.innerHTML.replace(/\D/g, '')
		cnpjs.push(e)
	})
	inputCnpj.value = cnpjs.join(',', ',')
}