import {createSlice} from "@reduxjs/toolkit";

let stock = createSlice({
	name: 'stock',
	initialState: [
		{id: 0, name: 'White and Black', count: 2},
		{id: 2, name: 'Grey Yordan', count: 1}
	],
	reducers: {
		setCount(state, action) {
			let id = action.payload;
			let find = state.find(e => e.id == id);
			find.count++;
		},
		addCart(state, action) {
			let item = action.payload;
			let find = state.find(e => e.id == item.id);
			if (find) {
				find.count++;
			}
			else {
				const item = {id: item.id, name: item.title, count:1};
				state.push(item);
			}
		},
		removeCart(state, action) {
			let itemId = action.payload;
			return state.filter(e => !(e.id == itemId));
		},
	}
});
export let {setCount, addCart, removeCart} = stock.actions;
export default stock;