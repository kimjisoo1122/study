/* eslint-disable */
import './App.css';
import {useState} from "react";

function App() {

	const [글제목, 글제목수정] = useState(['남자코트 추천', '강남 우동맛집', '파이썬독학']);
	const [따봉, 따봉변경] = useState([0, 0, 0]);
	const [modal, setModal] = useState(false);
	const [title, setTitle] = useState(0);
	const [입력값, 입력값수정] = useState('');

	return (
			<div>
				<header className='black-nav'>
					<h1>ReactBlog</h1>
				</header>
				{
					글제목.map((e, i) => {
						return (
								<div className='container' key={i}>
									<div className='list' >
										<h4 className='title' onClick={() => {
											setTitle(i);
											setModal(!modal);
										}
										}>{e} <span onClick={(e) => {
											e.stopPropagation();
											let 변경할따봉 = [...따봉];
											변경할따봉[i]++;
											따봉변경(변경할따봉)
										}}>👍 {따봉[i]}</span></h4>
										<p className='date'>2023년 2월 24일</p>
									</div>
									<div className='remove-container' >
										<button className='remove-btn' onClick={() => {
											const arr = [...글제목];
											console.log(arr[i]);
											arr.splice(i, 1);
											글제목수정(arr);
										}
										}>삭제버튼</button>
									</div>
								</div>
						)
					})
				}
				<input onChange={e => {
					입력값수정(e.target.value);
				}}/>
				<button onClick={() => {
					if (입력값) {
						const arr = [입력값, ...글제목];
						글제목수정(arr);
						document.querySelector('input').value = '';
					}

				}
				}>글발행</button>
				{
					modal ? <Modal title={title} 글제목={글제목} 글제목수정={글제목수정} /> : null
				}


			</div>
	);
}

function Modal(props) {
	return (
			<div className="modal">
				<h4>{props.글제목[props.title]}</h4>
				<p>날짜</p>
				<p>상세내용</p>
				<button>글수정</button>
			</div>
	)
}
class Modal2 extends React.Component {
	constructor(){
		super();
	}
	render(){
		return (
				<div>안녕</div>
		)
	}
}




export default App;
