import {Table} from "react-bootstrap";
import {useDispatch, useSelector} from "react-redux";
import {setAge} from "../store/userSlice";
import {removeCart, setCount} from "../store/stockSlice";
import {memo} from "react";

let Child = memo(function () {
	console.log('재렌더링');
	return <div>자식임</div>
})
function Cart() {

	let {stock, user} = useSelector(state => state);
	let dispatch = useDispatch();

	return (
			<div>


				{user.name}의 장바구니 나이 : {user.age}
				<button onClick={() => dispatch(setAge(20))}>버튼</button>

				<Table>
					<thead>
					<tr>
						<th>#</th>
						<th>상품명</th>
						<th>수량</th>
						<th>변경하기</th>
						<th>삭제하기</th>
					</tr>
					</thead>
					<tbody>
					{
						stock.map((e, i) => <CartRow key={i} item={e}></CartRow>)
					}
					</tbody>
				</Table>
			</div>
	)
}

function CartRow({item}) {
	let dispatch = useDispatch();
	return (
			<tr>
				<td>{item.id}</td>
				<td>{item.name}</td>
				<td>{item.count}</td>
				<td><button onClick={(e) => {
					dispatch(setCount(item.id))
				}}>+</button></td>
				<td><button onClick={(e) => {
					dispatch(removeCart(item.id))
				}}>X</button></td>
			</tr>
	)
}

export default Cart;