<c:set var="LoginLink" value="${pageContext.request.getSession(false).getAttribute('id') == null ? '/login/login' : '/login/logout'}"/>
<c:set var="Login" value="${pageContext.request.getSession(false).getAttribute('id') == null ? 'Login' : 'Logout'}"/>
<div id="menu">
  <ul>
    <li id="logo">fastcampus</li>
    <li><a href="<c:url value='/'/>">Home</a></li>
    <li><a href="<c:url value='/board/list'/>">Board</a></li>
    <li><a href="<c:url value="${LoginLink}"/>">${Login}</a></li>
    <li><a href="<c:url value='/register/add'/>">Sign in</a></li>
    <li><a href=""><i class="fas fa-search small"></i></a></li>
  </ul>
</div>