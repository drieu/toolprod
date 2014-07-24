<div class="list-group">
    <a href="#" class="list-group-item active">
        Frontaux WEB
    </a>
    <g:each in="${machines}" var="machine">
        <g:if test="${machine.name.contains('web')}">
            <g:if test="${!machine.name.contains('webexaco') && !machine.name.contains('webgrh')}">
                <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                    ${machine.name}
                </a>
            </g:if>
        </g:if>
    </g:each>
</div>