-- 用于物料库列头展示
UPDATE s_pdm_data.t_column_define
SET   dict_type='[{"label":"启用","value":"0"},{"label":"停用","value":"1"}]'
WHERE id='material21' and column_code='status';
