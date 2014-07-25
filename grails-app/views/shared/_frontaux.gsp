<div class="list-group">
    <a href="#" class="list-group-item active">
        Frontaux WEB
    </a>
    <g:each in="${machines}" var="machine">
        <g:if test="${machine.name.contains('web')}">
            <g:if test="${!machine.name.contains('webexaco') && !machine.name.contains('webgrh')}">
                <%
//                    boolean status = false
//                    try {
//                        def address = InetAddress.getByName(machine?.name);
//                        def timeoutMillis = 1000 ;
//                        if (address.isReachable(timeoutMillis)) {
//                            status = true
//                        }
//                    } catch( Exception e) {
//                        status = false
//                    }
                %>
                <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                    ${machine.name}
                    %{--<g:if test="${status==false}">--}%
                        %{--<span class="label label-danger">KO</span>--}%
                    %{--</g:if>--}%
                </a>
            </g:if>
        </g:if>
    </g:each>
</div>