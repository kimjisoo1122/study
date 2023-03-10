import {useNavigate, useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {Nav} from "react-bootstrap";
import {Context1} from "../App";
import {useDispatch} from "react-redux";
import {addCart} from "../store/stockSlice";
import {logDOM} from "@testing-library/react";

/* eslint-disable */

// let YelloBtn = styled.button`
//   background: ${props => props.bg};
//   color: ${props => props.bg == 'yellow' ? 'white' : 'black'};
//   padding: 10px;
// `;
function Detail(props) {

	const [discount, setDiscount] = useState(true);
	const [num, setNum] = useState(0);
	const [tab, setTab] = useState(0);
	const [ani, setAni] = useState('');
	let {재고, shoes} = useContext(Context1);
	let {id} = useParams();

	useEffect(() => {
		let timer = setTimeout(() => {
			setDiscount(false)
		}, 2000); // [] mount될때 1번만 실행

		setAni('')
		setTimeout(() => {
			setAni('end');
		}, 10);


		return () => {
			clearTimeout(timer);
			// useEffect실행전에 실행 clean up function 함수실행전에 청소 타이머는 보톹 기존 타이머 제거후
			// 리엑트는 자주 렌더링 되기에 useEffect사용할때 주의해야 서버에 요청할때 그전 요청은 취소~등등
			// clean up function은 mount에는 실행x update,unmount에만 실행 (컴포넌트가 옮겨지거나 할때)
			// 인자가 []일 경우는 mount에만 실행한다는 의미로 update에서 cleanupfunction 실행x
		}
		// useEffect는 side effect에서 나온 부가기능을 실행하기 위한 hook(라이브러리 함수)
		// 컴포넌트의 라이프사이클은 mount(최초부착) update(재랜더링) unmount(랜더링끝)가 있다
		// useEffect는 리엑트 특성상 자주 랜더링을 하기에 사용에 주의를 요함
		// useEffect의 마지막 파라미터인 []안에 들어간 변수나 state들이 변할때 마다 발동조건추가가능 빈 []는 mount시에만 1회만 이펙트사용
		// return은 mount시에는 작동안하고 update 되거나 unmount될때 effect사용전에 발동가능
		//(참고1) clean up function에는 타이머제거, socket 연결요청제거, ajax요청 중단 이런 코드를 많이 작성합니다.
		// (참고2) 컴포넌트 unmount 시에도 clean up function 안에 있던게 1회 실행됩니다.

	}, []);
	// detail화면이 뜰떄 opacity 0 -> 1 애니메이션
	useEffect(() => {
		return (() => {
					let watched = JSON.parse(localStorage.getItem('watched'));
					if (watched) {
						let set = new Set(watched);
						set.add(id);
						localStorage.setItem('watched', JSON.stringify([...set]))
					}
					else {
						let watched = JSON.stringify([id]);
						localStorage.setItem('watched', watched)

					}
		}
		);
	}, []);



	useEffect(() => {
		isNaN(num) ? alert('숫자입력') : null
	}, [num])

	let dispatch = useDispatch();
	let navigate = useNavigate();



	let find = props.shoes.find(e => e.id == id);
	return (
			<div className={`container start ${ani}`}>

				<input type="text" className="input-group-text" onChange={(e) => {
					setNum(e.target.value);
				}
				}></input>

				<div className="row">
					<div className="col-md-6">
						<img src={`https://codingapple1.github.io/shop/shoes${find.id + 1}.jpg`} width="100%"/>
					</div>
					<div className="col-md-6">
						<h4 className="pt-5">{find.title}</h4>
						<p>{find.content}</p>
						<p>{find.price}원</p>
						<button className="btn btn-danger" onClick={() => {
							dispatch(addCart(find));
							navigate('/cart');
						}
						}>주문하기</button>
					</div>
				</div>

				<Nav variant="tabs" defaultActiveKey="link0" onClick={e => {
					setTab(e.target.dataset.key);
				}
				}>
					<Nav.Item>
						<Nav.Link eventKey="link0" data-key={0}>버튼0</Nav.Link>
					</Nav.Item>
					<Nav.Item>
						<Nav.Link eventKey="link1" data-key={1}>버튼1</Nav.Link>
					</Nav.Item>
					<Nav.Item>
						<Nav.Link eventKey="link2" data-key={2}>버튼2</Nav.Link>
					</Nav.Item>
				</Nav>
				<Tab keys={tab} shoes={props.shoes}></Tab>
			</div>
	);
}

function Tab({keys, shoes}) {

	let {재고} = useContext(Context1);
	let [fade, setFade] = useState('fade');

	useEffect(() => {
		setFade('')
		setTimeout(() => {
			setFade('end');
		}, 10);

	}, [keys])

	return (<div style={{height: '300px'}} className={`start ${fade}`}>
				{
					[
						<div>내용0</div>,
						<div>내용1</div>,
						<div>내용2</div>
					][keys]
				}
			</div>
	)


}

export default Detail;