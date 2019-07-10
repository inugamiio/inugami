import { SelectItem } from '../common/selectitem';
export declare class ObjectUtils {
    static equals(obj1: any, obj2: any, field?: string): boolean;
    static equalsByValue(obj1: any, obj2: any): boolean;
    static resolveFieldData(data: any, field: any): any;
    static isFunction(obj: any): boolean;
    static filter(value: any[], fields: any[], filterValue: string): any[];
    static reorderArray(value: any[], from: number, to: number): void;
    static generateSelectItems(val: any[], field: string): SelectItem[];
    static insertIntoOrderedArray(item: any, index: number, arr: any[], sourceArr: any[]): void;
    static findIndexInList(item: any, list: any): number;
    static removeAccents(str: any): any;
}
