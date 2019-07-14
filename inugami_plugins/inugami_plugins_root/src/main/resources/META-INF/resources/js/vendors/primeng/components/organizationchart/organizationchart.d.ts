import { ElementRef, AfterContentInit, EventEmitter, TemplateRef, QueryList } from '@angular/core';
import { TreeNode } from '../common/treenode';
export declare class OrganizationChartNode {
    node: TreeNode;
    root: boolean;
    first: boolean;
    last: boolean;
    chart: OrganizationChart;
    constructor(chart: any);
    readonly leaf: boolean;
    readonly colspan: number;
    onNodeClick(event: Event, node: TreeNode): void;
    toggleNode(event: Event, node: TreeNode): void;
    isSelected(): boolean;
}
export declare class OrganizationChart implements AfterContentInit {
    el: ElementRef;
    value: TreeNode[];
    style: any;
    styleClass: string;
    selectionMode: string;
    selection: any;
    selectionChange: EventEmitter<any>;
    onNodeSelect: EventEmitter<any>;
    onNodeUnselect: EventEmitter<any>;
    onNodeExpand: EventEmitter<any>;
    onNodeCollapse: EventEmitter<any>;
    templates: QueryList<any>;
    templateMap: any;
    constructor(el: ElementRef);
    readonly root: TreeNode;
    ngAfterContentInit(): void;
    getTemplateForNode(node: TreeNode): TemplateRef<any>;
    onNodeClick(event: Event, node: TreeNode): void;
    findIndexInSelection(node: TreeNode): number;
    isSelected(node: TreeNode): boolean;
}
export declare class OrganizationChartModule {
}
