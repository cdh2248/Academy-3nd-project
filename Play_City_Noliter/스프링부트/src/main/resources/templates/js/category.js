const category = {
    name : "관광",
    keyword :["강화/233", "버스/200", "신라/100"]
};

export const getCategory = ()=>{
    return category;
};

export const useCategory = () =>{
    const category = getCategory();
    return category;
};