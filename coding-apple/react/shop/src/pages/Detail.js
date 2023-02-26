import {Link, useNavigate, useParams} from "react-router-dom";
import styled from "styled-components";
/* eslint-disable */

let YelloBtn = styled.button`
  background: ${props => props.bg};
  color: ${props => props.bg == 'yellow' ? 'white' : 'black'};
  padding: 10px;
`;

let NewBtn = styled.button(YelloBtn);

let Box = styled.div`
	background-color: grey;
	padding: 10px;
`;
function Detail(props) {
	let {id} = useParams();
	let find = props.shoes.find(e => e.id == id);
	return (
			<div className="container">
					<YelloBtn bg={'dodgerblue'}>버튼</YelloBtn>
					<YelloBtn bg={'yellow'}>버튼</YelloBtn>
					<NewBtn bg={'red'}>버튼</NewBtn>

				<div className="row">
					<div className="col-md-6">
						<img src={`https://codingapple1.github.io/shop/shoes${find.id + 1}.jpg`} width="100%"/>
					</div>
					<div className="col-md-6">
						<h4 className="pt-5">{find.title}</h4>
						<p>{find.content}</p>
						<p>{find.price}원</p>
						<button className="btn btn-danger">주문하기</button>
					</div>
				</div>
			</div>
	);
}

export default Detail;