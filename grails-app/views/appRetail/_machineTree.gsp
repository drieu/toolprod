<g:each in="${nodes?.getChildren()}" var="element">
    <g:if test="${element.isLeaf() == true}">
        <br/>
        FEUILLE : ${element.nodeData.name}
    </g:if>
    <g:else>
        <g:render template="/appRetail/machineTree" model="[nodes:element]"/>
        <br/>
        FEUILLE 2:${element.nodeData.name}
    </g:else>
</g:each>
<g:if test="${nodes?.parent == null}">
    <br/>PARENT : ${nodes?.nodeData?.name}
</g:if>

