<div class="list-group">
    <a href="#" class="list-group-item active">
        Machines
    </a>
    <g:each in="${machines}" var="machine">
        <g:if test="${!machine.name.startsWith('web') && !machine.name.startsWith('serid') && !machine.name.startsWith('wappsco')}">

            <a href="<g:createLink action="getMachineApps" params="[machine:machine.name]" />" class="list-group-item">
                ${machine.name}
            </a>
        </g:if>

    </g:each>
</div>