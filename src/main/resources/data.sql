INSERT INTO task.process_document_mapping (created_at, modified_at, version, process_name, document_type)
VALUES
    ('08-06-2022T12:00:00', '08-06-2022T12:00:00', 1, 'setAssignee', 'MTR_Agreement'),
    ('08-06-2022T12:00:00', '08-06-2022T12:00:00', 1, 'test-process', 'TEST_DOCUMENT'),
    ('08-06-2022T12:00:00', '08-06-2022T12:00:00', 1, 'quality_documents', 'QUALITY_DOCUMENTS');

INSERT INTO task.definition (created_at, modified_at, version, type, code, display_value, short_display_value, display_order, is_active)
VALUES
    ('08-06-2022T12:00:00', '08-06-2022T12:00:00', 1, 'TASK_STATUS', 'PENDING', 'Ожидает исполнения', 'Ожидает исполнения', 1, true),
    ('08-06-2022T12:00:00', '08-06-2022T12:00:00', 1, 'TASK_STATUS', 'IN_PROGRESS', 'В работе', 'В работе', 2, true),
    ('08-06-2022T12:00:00', '08-06-2022T12:00:00', 1, 'TASK_STATUS', 'COMPLETED', 'Выполнена', 'Выполнена', 3, true);

INSERT INTO task.definition (created_at, modified_at, version, type, code, display_value, short_display_value, display_order, is_active)
VALUES
    ('08-06-2022T12:00:00', '08-06-2022T12:00:00', 1, 'TASK_TYPE', 'APPROVAL', 'Согласование', 'Согласование', 1, true),
    ('08-06-2022T12:00:00', '08-06-2022T12:00:00', 1, 'TASK_TYPE', 'FAMILIARIZATION', 'Ознакомление', 'Ознакомление', 2, true);
